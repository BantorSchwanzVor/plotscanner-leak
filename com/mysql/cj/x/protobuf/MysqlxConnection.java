package com.mysql.cj.x.protobuf;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.RepeatedFieldBuilderV3;
import com.google.protobuf.SingleFieldBuilderV3;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MysqlxConnection {
  public static void registerAllExtensions(ExtensionRegistryLite registry) {}
  
  public static void registerAllExtensions(ExtensionRegistry registry) {
    registerAllExtensions((ExtensionRegistryLite)registry);
  }
  
  public static final class Capability extends GeneratedMessageV3 implements CapabilityOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private int bitField0_;
    
    public static final int NAME_FIELD_NUMBER = 1;
    
    private volatile Object name_;
    
    public static final int VALUE_FIELD_NUMBER = 2;
    
    private MysqlxDatatypes.Any value_;
    
    private byte memoizedIsInitialized;
    
    private Capability(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private Capability() {
      this.memoizedIsInitialized = -1;
      this.name_ = "";
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private Capability(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          ByteString bs;
          MysqlxDatatypes.Any.Builder subBuilder;
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 10:
              bs = input.readBytes();
              this.bitField0_ |= 0x1;
              this.name_ = bs;
              continue;
            case 18:
              subBuilder = null;
              if ((this.bitField0_ & 0x2) == 2)
                subBuilder = this.value_.toBuilder(); 
              this.value_ = (MysqlxDatatypes.Any)input.readMessage(MysqlxDatatypes.Any.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(this.value_);
                this.value_ = subBuilder.buildPartial();
              } 
              this.bitField0_ |= 0x2;
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_Capability_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_Capability_fieldAccessorTable.ensureFieldAccessorsInitialized(Capability.class, Builder.class);
    }
    
    public boolean hasName() {
      return ((this.bitField0_ & 0x1) == 1);
    }
    
    public String getName() {
      Object ref = this.name_;
      if (ref instanceof String)
        return (String)ref; 
      ByteString bs = (ByteString)ref;
      String s = bs.toStringUtf8();
      if (bs.isValidUtf8())
        this.name_ = s; 
      return s;
    }
    
    public ByteString getNameBytes() {
      Object ref = this.name_;
      if (ref instanceof String) {
        ByteString b = ByteString.copyFromUtf8((String)ref);
        this.name_ = b;
        return b;
      } 
      return (ByteString)ref;
    }
    
    public boolean hasValue() {
      return ((this.bitField0_ & 0x2) == 2);
    }
    
    public MysqlxDatatypes.Any getValue() {
      return (this.value_ == null) ? MysqlxDatatypes.Any.getDefaultInstance() : this.value_;
    }
    
    public MysqlxDatatypes.AnyOrBuilder getValueOrBuilder() {
      return (this.value_ == null) ? MysqlxDatatypes.Any.getDefaultInstance() : this.value_;
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      if (!hasName()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      if (!hasValue()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      if (!getValue().isInitialized()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      if ((this.bitField0_ & 0x1) == 1)
        GeneratedMessageV3.writeString(output, 1, this.name_); 
      if ((this.bitField0_ & 0x2) == 2)
        output.writeMessage(2, (MessageLite)getValue()); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      if ((this.bitField0_ & 0x1) == 1)
        size += GeneratedMessageV3.computeStringSize(1, this.name_); 
      if ((this.bitField0_ & 0x2) == 2)
        size += 
          CodedOutputStream.computeMessageSize(2, (MessageLite)getValue()); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof Capability))
        return super.equals(obj); 
      Capability other = (Capability)obj;
      boolean result = true;
      result = (result && hasName() == other.hasName());
      if (hasName())
        result = (result && getName().equals(other.getName())); 
      result = (result && hasValue() == other.hasValue());
      if (hasValue())
        result = (result && getValue().equals(other.getValue())); 
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (hasName()) {
        hash = 37 * hash + 1;
        hash = 53 * hash + getName().hashCode();
      } 
      if (hasValue()) {
        hash = 37 * hash + 2;
        hash = 53 * hash + getValue().hashCode();
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static Capability parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Capability)PARSER.parseFrom(data);
    }
    
    public static Capability parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Capability)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Capability parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Capability)PARSER.parseFrom(data);
    }
    
    public static Capability parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Capability)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Capability parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Capability)PARSER.parseFrom(data);
    }
    
    public static Capability parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Capability)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Capability parseFrom(InputStream input) throws IOException {
      return 
        (Capability)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Capability parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Capability)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Capability parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (Capability)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static Capability parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Capability)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Capability parseFrom(CodedInputStream input) throws IOException {
      return 
        (Capability)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Capability parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Capability)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(Capability prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxConnection.CapabilityOrBuilder {
      private int bitField0_;
      
      private Object name_;
      
      private MysqlxDatatypes.Any value_;
      
      private SingleFieldBuilderV3<MysqlxDatatypes.Any, MysqlxDatatypes.Any.Builder, MysqlxDatatypes.AnyOrBuilder> valueBuilder_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Capability_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Capability_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxConnection.Capability.class, Builder.class);
      }
      
      private Builder() {
        this.name_ = "";
        this.value_ = null;
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        this.name_ = "";
        this.value_ = null;
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxConnection.Capability.alwaysUseFieldBuilders)
          getValueFieldBuilder(); 
      }
      
      public Builder clear() {
        super.clear();
        this.name_ = "";
        this.bitField0_ &= 0xFFFFFFFE;
        if (this.valueBuilder_ == null) {
          this.value_ = null;
        } else {
          this.valueBuilder_.clear();
        } 
        this.bitField0_ &= 0xFFFFFFFD;
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Capability_descriptor;
      }
      
      public MysqlxConnection.Capability getDefaultInstanceForType() {
        return MysqlxConnection.Capability.getDefaultInstance();
      }
      
      public MysqlxConnection.Capability build() {
        MysqlxConnection.Capability result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxConnection.Capability buildPartial() {
        MysqlxConnection.Capability result = new MysqlxConnection.Capability(this);
        int from_bitField0_ = this.bitField0_;
        int to_bitField0_ = 0;
        if ((from_bitField0_ & 0x1) == 1)
          to_bitField0_ |= 0x1; 
        result.name_ = this.name_;
        if ((from_bitField0_ & 0x2) == 2)
          to_bitField0_ |= 0x2; 
        if (this.valueBuilder_ == null) {
          result.value_ = this.value_;
        } else {
          result.value_ = (MysqlxDatatypes.Any)this.valueBuilder_.build();
        } 
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxConnection.Capability)
          return mergeFrom((MysqlxConnection.Capability)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxConnection.Capability other) {
        if (other == MysqlxConnection.Capability.getDefaultInstance())
          return this; 
        if (other.hasName()) {
          this.bitField0_ |= 0x1;
          this.name_ = other.name_;
          onChanged();
        } 
        if (other.hasValue())
          mergeValue(other.getValue()); 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasName())
          return false; 
        if (!hasValue())
          return false; 
        if (!getValue().isInitialized())
          return false; 
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxConnection.Capability parsedMessage = null;
        try {
          parsedMessage = (MysqlxConnection.Capability)MysqlxConnection.Capability.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxConnection.Capability)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public boolean hasName() {
        return ((this.bitField0_ & 0x1) == 1);
      }
      
      public String getName() {
        Object ref = this.name_;
        if (!(ref instanceof String)) {
          ByteString bs = (ByteString)ref;
          String s = bs.toStringUtf8();
          if (bs.isValidUtf8())
            this.name_ = s; 
          return s;
        } 
        return (String)ref;
      }
      
      public ByteString getNameBytes() {
        Object ref = this.name_;
        if (ref instanceof String) {
          ByteString b = ByteString.copyFromUtf8((String)ref);
          this.name_ = b;
          return b;
        } 
        return (ByteString)ref;
      }
      
      public Builder setName(String value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.name_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearName() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.name_ = MysqlxConnection.Capability.getDefaultInstance().getName();
        onChanged();
        return this;
      }
      
      public Builder setNameBytes(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.name_ = value;
        onChanged();
        return this;
      }
      
      public boolean hasValue() {
        return ((this.bitField0_ & 0x2) == 2);
      }
      
      public MysqlxDatatypes.Any getValue() {
        if (this.valueBuilder_ == null)
          return (this.value_ == null) ? MysqlxDatatypes.Any.getDefaultInstance() : this.value_; 
        return (MysqlxDatatypes.Any)this.valueBuilder_.getMessage();
      }
      
      public Builder setValue(MysqlxDatatypes.Any value) {
        if (this.valueBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          this.value_ = value;
          onChanged();
        } else {
          this.valueBuilder_.setMessage((AbstractMessage)value);
        } 
        this.bitField0_ |= 0x2;
        return this;
      }
      
      public Builder setValue(MysqlxDatatypes.Any.Builder builderForValue) {
        if (this.valueBuilder_ == null) {
          this.value_ = builderForValue.build();
          onChanged();
        } else {
          this.valueBuilder_.setMessage((AbstractMessage)builderForValue.build());
        } 
        this.bitField0_ |= 0x2;
        return this;
      }
      
      public Builder mergeValue(MysqlxDatatypes.Any value) {
        if (this.valueBuilder_ == null) {
          if ((this.bitField0_ & 0x2) == 2 && this.value_ != null && this.value_ != 
            
            MysqlxDatatypes.Any.getDefaultInstance()) {
            this
              .value_ = MysqlxDatatypes.Any.newBuilder(this.value_).mergeFrom(value).buildPartial();
          } else {
            this.value_ = value;
          } 
          onChanged();
        } else {
          this.valueBuilder_.mergeFrom((AbstractMessage)value);
        } 
        this.bitField0_ |= 0x2;
        return this;
      }
      
      public Builder clearValue() {
        if (this.valueBuilder_ == null) {
          this.value_ = null;
          onChanged();
        } else {
          this.valueBuilder_.clear();
        } 
        this.bitField0_ &= 0xFFFFFFFD;
        return this;
      }
      
      public MysqlxDatatypes.Any.Builder getValueBuilder() {
        this.bitField0_ |= 0x2;
        onChanged();
        return (MysqlxDatatypes.Any.Builder)getValueFieldBuilder().getBuilder();
      }
      
      public MysqlxDatatypes.AnyOrBuilder getValueOrBuilder() {
        if (this.valueBuilder_ != null)
          return (MysqlxDatatypes.AnyOrBuilder)this.valueBuilder_.getMessageOrBuilder(); 
        return (this.value_ == null) ? 
          MysqlxDatatypes.Any.getDefaultInstance() : this.value_;
      }
      
      private SingleFieldBuilderV3<MysqlxDatatypes.Any, MysqlxDatatypes.Any.Builder, MysqlxDatatypes.AnyOrBuilder> getValueFieldBuilder() {
        if (this.valueBuilder_ == null) {
          this
            
            .valueBuilder_ = new SingleFieldBuilderV3((AbstractMessage)getValue(), (AbstractMessage.BuilderParent)getParentForChildren(), isClean());
          this.value_ = null;
        } 
        return this.valueBuilder_;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final Capability DEFAULT_INSTANCE = new Capability();
    
    public static Capability getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<Capability> PARSER = (Parser<Capability>)new AbstractParser<Capability>() {
        public MysqlxConnection.Capability parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxConnection.Capability(input, extensionRegistry);
        }
      };
    
    public static Parser<Capability> parser() {
      return PARSER;
    }
    
    public Parser<Capability> getParserForType() {
      return PARSER;
    }
    
    public Capability getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class Capabilities extends GeneratedMessageV3 implements CapabilitiesOrBuilder {
    private static final long serialVersionUID = 0L;
    
    public static final int CAPABILITIES_FIELD_NUMBER = 1;
    
    private List<MysqlxConnection.Capability> capabilities_;
    
    private byte memoizedIsInitialized;
    
    private Capabilities(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private Capabilities() {
      this.memoizedIsInitialized = -1;
      this.capabilities_ = Collections.emptyList();
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private Capabilities(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 10:
              if ((mutable_bitField0_ & 0x1) != 1) {
                this.capabilities_ = new ArrayList<>();
                mutable_bitField0_ |= 0x1;
              } 
              this.capabilities_.add(input.readMessage(MysqlxConnection.Capability.PARSER, extensionRegistry));
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        if ((mutable_bitField0_ & 0x1) == 1)
          this.capabilities_ = Collections.unmodifiableList(this.capabilities_); 
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_Capabilities_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_Capabilities_fieldAccessorTable.ensureFieldAccessorsInitialized(Capabilities.class, Builder.class);
    }
    
    public List<MysqlxConnection.Capability> getCapabilitiesList() {
      return this.capabilities_;
    }
    
    public List<? extends MysqlxConnection.CapabilityOrBuilder> getCapabilitiesOrBuilderList() {
      return (List)this.capabilities_;
    }
    
    public int getCapabilitiesCount() {
      return this.capabilities_.size();
    }
    
    public MysqlxConnection.Capability getCapabilities(int index) {
      return this.capabilities_.get(index);
    }
    
    public MysqlxConnection.CapabilityOrBuilder getCapabilitiesOrBuilder(int index) {
      return this.capabilities_.get(index);
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      for (int i = 0; i < getCapabilitiesCount(); i++) {
        if (!getCapabilities(i).isInitialized()) {
          this.memoizedIsInitialized = 0;
          return false;
        } 
      } 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      for (int i = 0; i < this.capabilities_.size(); i++)
        output.writeMessage(1, (MessageLite)this.capabilities_.get(i)); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      for (int i = 0; i < this.capabilities_.size(); i++)
        size += 
          CodedOutputStream.computeMessageSize(1, (MessageLite)this.capabilities_.get(i)); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof Capabilities))
        return super.equals(obj); 
      Capabilities other = (Capabilities)obj;
      boolean result = true;
      result = (result && getCapabilitiesList().equals(other.getCapabilitiesList()));
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (getCapabilitiesCount() > 0) {
        hash = 37 * hash + 1;
        hash = 53 * hash + getCapabilitiesList().hashCode();
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static Capabilities parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Capabilities)PARSER.parseFrom(data);
    }
    
    public static Capabilities parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Capabilities)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Capabilities parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Capabilities)PARSER.parseFrom(data);
    }
    
    public static Capabilities parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Capabilities)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Capabilities parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Capabilities)PARSER.parseFrom(data);
    }
    
    public static Capabilities parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Capabilities)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Capabilities parseFrom(InputStream input) throws IOException {
      return 
        (Capabilities)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Capabilities parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Capabilities)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Capabilities parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (Capabilities)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static Capabilities parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Capabilities)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Capabilities parseFrom(CodedInputStream input) throws IOException {
      return 
        (Capabilities)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Capabilities parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Capabilities)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(Capabilities prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxConnection.CapabilitiesOrBuilder {
      private int bitField0_;
      
      private List<MysqlxConnection.Capability> capabilities_;
      
      private RepeatedFieldBuilderV3<MysqlxConnection.Capability, MysqlxConnection.Capability.Builder, MysqlxConnection.CapabilityOrBuilder> capabilitiesBuilder_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Capabilities_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Capabilities_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxConnection.Capabilities.class, Builder.class);
      }
      
      private Builder() {
        this
          .capabilities_ = Collections.emptyList();
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        this.capabilities_ = Collections.emptyList();
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxConnection.Capabilities.alwaysUseFieldBuilders)
          getCapabilitiesFieldBuilder(); 
      }
      
      public Builder clear() {
        super.clear();
        if (this.capabilitiesBuilder_ == null) {
          this.capabilities_ = Collections.emptyList();
          this.bitField0_ &= 0xFFFFFFFE;
        } else {
          this.capabilitiesBuilder_.clear();
        } 
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Capabilities_descriptor;
      }
      
      public MysqlxConnection.Capabilities getDefaultInstanceForType() {
        return MysqlxConnection.Capabilities.getDefaultInstance();
      }
      
      public MysqlxConnection.Capabilities build() {
        MysqlxConnection.Capabilities result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxConnection.Capabilities buildPartial() {
        MysqlxConnection.Capabilities result = new MysqlxConnection.Capabilities(this);
        int from_bitField0_ = this.bitField0_;
        if (this.capabilitiesBuilder_ == null) {
          if ((this.bitField0_ & 0x1) == 1) {
            this.capabilities_ = Collections.unmodifiableList(this.capabilities_);
            this.bitField0_ &= 0xFFFFFFFE;
          } 
          result.capabilities_ = this.capabilities_;
        } else {
          result.capabilities_ = this.capabilitiesBuilder_.build();
        } 
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxConnection.Capabilities)
          return mergeFrom((MysqlxConnection.Capabilities)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxConnection.Capabilities other) {
        if (other == MysqlxConnection.Capabilities.getDefaultInstance())
          return this; 
        if (this.capabilitiesBuilder_ == null) {
          if (!other.capabilities_.isEmpty()) {
            if (this.capabilities_.isEmpty()) {
              this.capabilities_ = other.capabilities_;
              this.bitField0_ &= 0xFFFFFFFE;
            } else {
              ensureCapabilitiesIsMutable();
              this.capabilities_.addAll(other.capabilities_);
            } 
            onChanged();
          } 
        } else if (!other.capabilities_.isEmpty()) {
          if (this.capabilitiesBuilder_.isEmpty()) {
            this.capabilitiesBuilder_.dispose();
            this.capabilitiesBuilder_ = null;
            this.capabilities_ = other.capabilities_;
            this.bitField0_ &= 0xFFFFFFFE;
            this.capabilitiesBuilder_ = MysqlxConnection.Capabilities.alwaysUseFieldBuilders ? getCapabilitiesFieldBuilder() : null;
          } else {
            this.capabilitiesBuilder_.addAllMessages(other.capabilities_);
          } 
        } 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        for (int i = 0; i < getCapabilitiesCount(); i++) {
          if (!getCapabilities(i).isInitialized())
            return false; 
        } 
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxConnection.Capabilities parsedMessage = null;
        try {
          parsedMessage = (MysqlxConnection.Capabilities)MysqlxConnection.Capabilities.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxConnection.Capabilities)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      private void ensureCapabilitiesIsMutable() {
        if ((this.bitField0_ & 0x1) != 1) {
          this.capabilities_ = new ArrayList<>(this.capabilities_);
          this.bitField0_ |= 0x1;
        } 
      }
      
      public List<MysqlxConnection.Capability> getCapabilitiesList() {
        if (this.capabilitiesBuilder_ == null)
          return Collections.unmodifiableList(this.capabilities_); 
        return this.capabilitiesBuilder_.getMessageList();
      }
      
      public int getCapabilitiesCount() {
        if (this.capabilitiesBuilder_ == null)
          return this.capabilities_.size(); 
        return this.capabilitiesBuilder_.getCount();
      }
      
      public MysqlxConnection.Capability getCapabilities(int index) {
        if (this.capabilitiesBuilder_ == null)
          return this.capabilities_.get(index); 
        return (MysqlxConnection.Capability)this.capabilitiesBuilder_.getMessage(index);
      }
      
      public Builder setCapabilities(int index, MysqlxConnection.Capability value) {
        if (this.capabilitiesBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          ensureCapabilitiesIsMutable();
          this.capabilities_.set(index, value);
          onChanged();
        } else {
          this.capabilitiesBuilder_.setMessage(index, (AbstractMessage)value);
        } 
        return this;
      }
      
      public Builder setCapabilities(int index, MysqlxConnection.Capability.Builder builderForValue) {
        if (this.capabilitiesBuilder_ == null) {
          ensureCapabilitiesIsMutable();
          this.capabilities_.set(index, builderForValue.build());
          onChanged();
        } else {
          this.capabilitiesBuilder_.setMessage(index, (AbstractMessage)builderForValue.build());
        } 
        return this;
      }
      
      public Builder addCapabilities(MysqlxConnection.Capability value) {
        if (this.capabilitiesBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          ensureCapabilitiesIsMutable();
          this.capabilities_.add(value);
          onChanged();
        } else {
          this.capabilitiesBuilder_.addMessage((AbstractMessage)value);
        } 
        return this;
      }
      
      public Builder addCapabilities(int index, MysqlxConnection.Capability value) {
        if (this.capabilitiesBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          ensureCapabilitiesIsMutable();
          this.capabilities_.add(index, value);
          onChanged();
        } else {
          this.capabilitiesBuilder_.addMessage(index, (AbstractMessage)value);
        } 
        return this;
      }
      
      public Builder addCapabilities(MysqlxConnection.Capability.Builder builderForValue) {
        if (this.capabilitiesBuilder_ == null) {
          ensureCapabilitiesIsMutable();
          this.capabilities_.add(builderForValue.build());
          onChanged();
        } else {
          this.capabilitiesBuilder_.addMessage((AbstractMessage)builderForValue.build());
        } 
        return this;
      }
      
      public Builder addCapabilities(int index, MysqlxConnection.Capability.Builder builderForValue) {
        if (this.capabilitiesBuilder_ == null) {
          ensureCapabilitiesIsMutable();
          this.capabilities_.add(index, builderForValue.build());
          onChanged();
        } else {
          this.capabilitiesBuilder_.addMessage(index, (AbstractMessage)builderForValue.build());
        } 
        return this;
      }
      
      public Builder addAllCapabilities(Iterable<? extends MysqlxConnection.Capability> values) {
        if (this.capabilitiesBuilder_ == null) {
          ensureCapabilitiesIsMutable();
          AbstractMessageLite.Builder.addAll(values, this.capabilities_);
          onChanged();
        } else {
          this.capabilitiesBuilder_.addAllMessages(values);
        } 
        return this;
      }
      
      public Builder clearCapabilities() {
        if (this.capabilitiesBuilder_ == null) {
          this.capabilities_ = Collections.emptyList();
          this.bitField0_ &= 0xFFFFFFFE;
          onChanged();
        } else {
          this.capabilitiesBuilder_.clear();
        } 
        return this;
      }
      
      public Builder removeCapabilities(int index) {
        if (this.capabilitiesBuilder_ == null) {
          ensureCapabilitiesIsMutable();
          this.capabilities_.remove(index);
          onChanged();
        } else {
          this.capabilitiesBuilder_.remove(index);
        } 
        return this;
      }
      
      public MysqlxConnection.Capability.Builder getCapabilitiesBuilder(int index) {
        return (MysqlxConnection.Capability.Builder)getCapabilitiesFieldBuilder().getBuilder(index);
      }
      
      public MysqlxConnection.CapabilityOrBuilder getCapabilitiesOrBuilder(int index) {
        if (this.capabilitiesBuilder_ == null)
          return this.capabilities_.get(index); 
        return (MysqlxConnection.CapabilityOrBuilder)this.capabilitiesBuilder_.getMessageOrBuilder(index);
      }
      
      public List<? extends MysqlxConnection.CapabilityOrBuilder> getCapabilitiesOrBuilderList() {
        if (this.capabilitiesBuilder_ != null)
          return this.capabilitiesBuilder_.getMessageOrBuilderList(); 
        return Collections.unmodifiableList((List)this.capabilities_);
      }
      
      public MysqlxConnection.Capability.Builder addCapabilitiesBuilder() {
        return (MysqlxConnection.Capability.Builder)getCapabilitiesFieldBuilder().addBuilder(
            (AbstractMessage)MysqlxConnection.Capability.getDefaultInstance());
      }
      
      public MysqlxConnection.Capability.Builder addCapabilitiesBuilder(int index) {
        return (MysqlxConnection.Capability.Builder)getCapabilitiesFieldBuilder().addBuilder(index, 
            (AbstractMessage)MysqlxConnection.Capability.getDefaultInstance());
      }
      
      public List<MysqlxConnection.Capability.Builder> getCapabilitiesBuilderList() {
        return getCapabilitiesFieldBuilder().getBuilderList();
      }
      
      private RepeatedFieldBuilderV3<MysqlxConnection.Capability, MysqlxConnection.Capability.Builder, MysqlxConnection.CapabilityOrBuilder> getCapabilitiesFieldBuilder() {
        if (this.capabilitiesBuilder_ == null) {
          this
            
            .capabilitiesBuilder_ = new RepeatedFieldBuilderV3(this.capabilities_, ((this.bitField0_ & 0x1) == 1), (AbstractMessage.BuilderParent)getParentForChildren(), isClean());
          this.capabilities_ = null;
        } 
        return this.capabilitiesBuilder_;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final Capabilities DEFAULT_INSTANCE = new Capabilities();
    
    public static Capabilities getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<Capabilities> PARSER = (Parser<Capabilities>)new AbstractParser<Capabilities>() {
        public MysqlxConnection.Capabilities parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxConnection.Capabilities(input, extensionRegistry);
        }
      };
    
    public static Parser<Capabilities> parser() {
      return PARSER;
    }
    
    public Parser<Capabilities> getParserForType() {
      return PARSER;
    }
    
    public Capabilities getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class CapabilitiesGet extends GeneratedMessageV3 implements CapabilitiesGetOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private byte memoizedIsInitialized;
    
    private CapabilitiesGet(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private CapabilitiesGet() {
      this.memoizedIsInitialized = -1;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private CapabilitiesGet(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesGet_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesGet_fieldAccessorTable.ensureFieldAccessorsInitialized(CapabilitiesGet.class, Builder.class);
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof CapabilitiesGet))
        return super.equals(obj); 
      CapabilitiesGet other = (CapabilitiesGet)obj;
      boolean result = true;
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static CapabilitiesGet parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (CapabilitiesGet)PARSER.parseFrom(data);
    }
    
    public static CapabilitiesGet parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (CapabilitiesGet)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static CapabilitiesGet parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (CapabilitiesGet)PARSER.parseFrom(data);
    }
    
    public static CapabilitiesGet parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (CapabilitiesGet)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static CapabilitiesGet parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (CapabilitiesGet)PARSER.parseFrom(data);
    }
    
    public static CapabilitiesGet parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (CapabilitiesGet)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static CapabilitiesGet parseFrom(InputStream input) throws IOException {
      return 
        (CapabilitiesGet)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static CapabilitiesGet parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (CapabilitiesGet)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static CapabilitiesGet parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (CapabilitiesGet)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static CapabilitiesGet parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (CapabilitiesGet)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static CapabilitiesGet parseFrom(CodedInputStream input) throws IOException {
      return 
        (CapabilitiesGet)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static CapabilitiesGet parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (CapabilitiesGet)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(CapabilitiesGet prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxConnection.CapabilitiesGetOrBuilder {
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesGet_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesGet_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxConnection.CapabilitiesGet.class, Builder.class);
      }
      
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxConnection.CapabilitiesGet.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesGet_descriptor;
      }
      
      public MysqlxConnection.CapabilitiesGet getDefaultInstanceForType() {
        return MysqlxConnection.CapabilitiesGet.getDefaultInstance();
      }
      
      public MysqlxConnection.CapabilitiesGet build() {
        MysqlxConnection.CapabilitiesGet result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxConnection.CapabilitiesGet buildPartial() {
        MysqlxConnection.CapabilitiesGet result = new MysqlxConnection.CapabilitiesGet(this);
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxConnection.CapabilitiesGet)
          return mergeFrom((MysqlxConnection.CapabilitiesGet)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxConnection.CapabilitiesGet other) {
        if (other == MysqlxConnection.CapabilitiesGet.getDefaultInstance())
          return this; 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxConnection.CapabilitiesGet parsedMessage = null;
        try {
          parsedMessage = (MysqlxConnection.CapabilitiesGet)MysqlxConnection.CapabilitiesGet.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxConnection.CapabilitiesGet)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final CapabilitiesGet DEFAULT_INSTANCE = new CapabilitiesGet();
    
    public static CapabilitiesGet getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<CapabilitiesGet> PARSER = (Parser<CapabilitiesGet>)new AbstractParser<CapabilitiesGet>() {
        public MysqlxConnection.CapabilitiesGet parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxConnection.CapabilitiesGet(input, extensionRegistry);
        }
      };
    
    public static Parser<CapabilitiesGet> parser() {
      return PARSER;
    }
    
    public Parser<CapabilitiesGet> getParserForType() {
      return PARSER;
    }
    
    public CapabilitiesGet getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class CapabilitiesSet extends GeneratedMessageV3 implements CapabilitiesSetOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private int bitField0_;
    
    public static final int CAPABILITIES_FIELD_NUMBER = 1;
    
    private MysqlxConnection.Capabilities capabilities_;
    
    private byte memoizedIsInitialized;
    
    private CapabilitiesSet(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private CapabilitiesSet() {
      this.memoizedIsInitialized = -1;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private CapabilitiesSet(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          MysqlxConnection.Capabilities.Builder subBuilder;
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 10:
              subBuilder = null;
              if ((this.bitField0_ & 0x1) == 1)
                subBuilder = this.capabilities_.toBuilder(); 
              this.capabilities_ = (MysqlxConnection.Capabilities)input.readMessage(MysqlxConnection.Capabilities.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(this.capabilities_);
                this.capabilities_ = subBuilder.buildPartial();
              } 
              this.bitField0_ |= 0x1;
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesSet_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesSet_fieldAccessorTable.ensureFieldAccessorsInitialized(CapabilitiesSet.class, Builder.class);
    }
    
    public boolean hasCapabilities() {
      return ((this.bitField0_ & 0x1) == 1);
    }
    
    public MysqlxConnection.Capabilities getCapabilities() {
      return (this.capabilities_ == null) ? MysqlxConnection.Capabilities.getDefaultInstance() : this.capabilities_;
    }
    
    public MysqlxConnection.CapabilitiesOrBuilder getCapabilitiesOrBuilder() {
      return (this.capabilities_ == null) ? MysqlxConnection.Capabilities.getDefaultInstance() : this.capabilities_;
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      if (!hasCapabilities()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      if (!getCapabilities().isInitialized()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      if ((this.bitField0_ & 0x1) == 1)
        output.writeMessage(1, (MessageLite)getCapabilities()); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      if ((this.bitField0_ & 0x1) == 1)
        size += 
          CodedOutputStream.computeMessageSize(1, (MessageLite)getCapabilities()); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof CapabilitiesSet))
        return super.equals(obj); 
      CapabilitiesSet other = (CapabilitiesSet)obj;
      boolean result = true;
      result = (result && hasCapabilities() == other.hasCapabilities());
      if (hasCapabilities())
        result = (result && getCapabilities().equals(other.getCapabilities())); 
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (hasCapabilities()) {
        hash = 37 * hash + 1;
        hash = 53 * hash + getCapabilities().hashCode();
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static CapabilitiesSet parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (CapabilitiesSet)PARSER.parseFrom(data);
    }
    
    public static CapabilitiesSet parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (CapabilitiesSet)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static CapabilitiesSet parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (CapabilitiesSet)PARSER.parseFrom(data);
    }
    
    public static CapabilitiesSet parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (CapabilitiesSet)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static CapabilitiesSet parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (CapabilitiesSet)PARSER.parseFrom(data);
    }
    
    public static CapabilitiesSet parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (CapabilitiesSet)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static CapabilitiesSet parseFrom(InputStream input) throws IOException {
      return 
        (CapabilitiesSet)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static CapabilitiesSet parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (CapabilitiesSet)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static CapabilitiesSet parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (CapabilitiesSet)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static CapabilitiesSet parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (CapabilitiesSet)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static CapabilitiesSet parseFrom(CodedInputStream input) throws IOException {
      return 
        (CapabilitiesSet)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static CapabilitiesSet parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (CapabilitiesSet)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(CapabilitiesSet prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxConnection.CapabilitiesSetOrBuilder {
      private int bitField0_;
      
      private MysqlxConnection.Capabilities capabilities_;
      
      private SingleFieldBuilderV3<MysqlxConnection.Capabilities, MysqlxConnection.Capabilities.Builder, MysqlxConnection.CapabilitiesOrBuilder> capabilitiesBuilder_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesSet_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesSet_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxConnection.CapabilitiesSet.class, Builder.class);
      }
      
      private Builder() {
        this.capabilities_ = null;
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        this.capabilities_ = null;
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxConnection.CapabilitiesSet.alwaysUseFieldBuilders)
          getCapabilitiesFieldBuilder(); 
      }
      
      public Builder clear() {
        super.clear();
        if (this.capabilitiesBuilder_ == null) {
          this.capabilities_ = null;
        } else {
          this.capabilitiesBuilder_.clear();
        } 
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_CapabilitiesSet_descriptor;
      }
      
      public MysqlxConnection.CapabilitiesSet getDefaultInstanceForType() {
        return MysqlxConnection.CapabilitiesSet.getDefaultInstance();
      }
      
      public MysqlxConnection.CapabilitiesSet build() {
        MysqlxConnection.CapabilitiesSet result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxConnection.CapabilitiesSet buildPartial() {
        MysqlxConnection.CapabilitiesSet result = new MysqlxConnection.CapabilitiesSet(this);
        int from_bitField0_ = this.bitField0_;
        int to_bitField0_ = 0;
        if ((from_bitField0_ & 0x1) == 1)
          to_bitField0_ |= 0x1; 
        if (this.capabilitiesBuilder_ == null) {
          result.capabilities_ = this.capabilities_;
        } else {
          result.capabilities_ = (MysqlxConnection.Capabilities)this.capabilitiesBuilder_.build();
        } 
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxConnection.CapabilitiesSet)
          return mergeFrom((MysqlxConnection.CapabilitiesSet)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxConnection.CapabilitiesSet other) {
        if (other == MysqlxConnection.CapabilitiesSet.getDefaultInstance())
          return this; 
        if (other.hasCapabilities())
          mergeCapabilities(other.getCapabilities()); 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasCapabilities())
          return false; 
        if (!getCapabilities().isInitialized())
          return false; 
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxConnection.CapabilitiesSet parsedMessage = null;
        try {
          parsedMessage = (MysqlxConnection.CapabilitiesSet)MysqlxConnection.CapabilitiesSet.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxConnection.CapabilitiesSet)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public boolean hasCapabilities() {
        return ((this.bitField0_ & 0x1) == 1);
      }
      
      public MysqlxConnection.Capabilities getCapabilities() {
        if (this.capabilitiesBuilder_ == null)
          return (this.capabilities_ == null) ? MysqlxConnection.Capabilities.getDefaultInstance() : this.capabilities_; 
        return (MysqlxConnection.Capabilities)this.capabilitiesBuilder_.getMessage();
      }
      
      public Builder setCapabilities(MysqlxConnection.Capabilities value) {
        if (this.capabilitiesBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          this.capabilities_ = value;
          onChanged();
        } else {
          this.capabilitiesBuilder_.setMessage((AbstractMessage)value);
        } 
        this.bitField0_ |= 0x1;
        return this;
      }
      
      public Builder setCapabilities(MysqlxConnection.Capabilities.Builder builderForValue) {
        if (this.capabilitiesBuilder_ == null) {
          this.capabilities_ = builderForValue.build();
          onChanged();
        } else {
          this.capabilitiesBuilder_.setMessage((AbstractMessage)builderForValue.build());
        } 
        this.bitField0_ |= 0x1;
        return this;
      }
      
      public Builder mergeCapabilities(MysqlxConnection.Capabilities value) {
        if (this.capabilitiesBuilder_ == null) {
          if ((this.bitField0_ & 0x1) == 1 && this.capabilities_ != null && this.capabilities_ != 
            
            MysqlxConnection.Capabilities.getDefaultInstance()) {
            this
              .capabilities_ = MysqlxConnection.Capabilities.newBuilder(this.capabilities_).mergeFrom(value).buildPartial();
          } else {
            this.capabilities_ = value;
          } 
          onChanged();
        } else {
          this.capabilitiesBuilder_.mergeFrom((AbstractMessage)value);
        } 
        this.bitField0_ |= 0x1;
        return this;
      }
      
      public Builder clearCapabilities() {
        if (this.capabilitiesBuilder_ == null) {
          this.capabilities_ = null;
          onChanged();
        } else {
          this.capabilitiesBuilder_.clear();
        } 
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
      }
      
      public MysqlxConnection.Capabilities.Builder getCapabilitiesBuilder() {
        this.bitField0_ |= 0x1;
        onChanged();
        return (MysqlxConnection.Capabilities.Builder)getCapabilitiesFieldBuilder().getBuilder();
      }
      
      public MysqlxConnection.CapabilitiesOrBuilder getCapabilitiesOrBuilder() {
        if (this.capabilitiesBuilder_ != null)
          return (MysqlxConnection.CapabilitiesOrBuilder)this.capabilitiesBuilder_.getMessageOrBuilder(); 
        return (this.capabilities_ == null) ? 
          MysqlxConnection.Capabilities.getDefaultInstance() : this.capabilities_;
      }
      
      private SingleFieldBuilderV3<MysqlxConnection.Capabilities, MysqlxConnection.Capabilities.Builder, MysqlxConnection.CapabilitiesOrBuilder> getCapabilitiesFieldBuilder() {
        if (this.capabilitiesBuilder_ == null) {
          this
            
            .capabilitiesBuilder_ = new SingleFieldBuilderV3((AbstractMessage)getCapabilities(), (AbstractMessage.BuilderParent)getParentForChildren(), isClean());
          this.capabilities_ = null;
        } 
        return this.capabilitiesBuilder_;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final CapabilitiesSet DEFAULT_INSTANCE = new CapabilitiesSet();
    
    public static CapabilitiesSet getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<CapabilitiesSet> PARSER = (Parser<CapabilitiesSet>)new AbstractParser<CapabilitiesSet>() {
        public MysqlxConnection.CapabilitiesSet parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxConnection.CapabilitiesSet(input, extensionRegistry);
        }
      };
    
    public static Parser<CapabilitiesSet> parser() {
      return PARSER;
    }
    
    public Parser<CapabilitiesSet> getParserForType() {
      return PARSER;
    }
    
    public CapabilitiesSet getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class Close extends GeneratedMessageV3 implements CloseOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private byte memoizedIsInitialized;
    
    private Close(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private Close() {
      this.memoizedIsInitialized = -1;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private Close(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_Close_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxConnection.internal_static_Mysqlx_Connection_Close_fieldAccessorTable.ensureFieldAccessorsInitialized(Close.class, Builder.class);
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof Close))
        return super.equals(obj); 
      Close other = (Close)obj;
      boolean result = true;
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static Close parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data);
    }
    
    public static Close parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Close parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data);
    }
    
    public static Close parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Close parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data);
    }
    
    public static Close parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Close parseFrom(InputStream input) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Close parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Close parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static Close parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Close parseFrom(CodedInputStream input) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Close parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(Close prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxConnection.CloseOrBuilder {
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Close_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Close_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxConnection.Close.class, Builder.class);
      }
      
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxConnection.Close.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxConnection.internal_static_Mysqlx_Connection_Close_descriptor;
      }
      
      public MysqlxConnection.Close getDefaultInstanceForType() {
        return MysqlxConnection.Close.getDefaultInstance();
      }
      
      public MysqlxConnection.Close build() {
        MysqlxConnection.Close result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxConnection.Close buildPartial() {
        MysqlxConnection.Close result = new MysqlxConnection.Close(this);
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxConnection.Close)
          return mergeFrom((MysqlxConnection.Close)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxConnection.Close other) {
        if (other == MysqlxConnection.Close.getDefaultInstance())
          return this; 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxConnection.Close parsedMessage = null;
        try {
          parsedMessage = (MysqlxConnection.Close)MysqlxConnection.Close.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxConnection.Close)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final Close DEFAULT_INSTANCE = new Close();
    
    public static Close getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<Close> PARSER = (Parser<Close>)new AbstractParser<Close>() {
        public MysqlxConnection.Close parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxConnection.Close(input, extensionRegistry);
        }
      };
    
    public static Parser<Close> parser() {
      return PARSER;
    }
    
    public Parser<Close> getParserForType() {
      return PARSER;
    }
    
    public Close getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }
  
  static {
    String[] descriptorData = { "\n\027mysqlx_connection.proto\022\021Mysqlx.Connection\032\026mysqlx_datatypes.proto\032\fmysqlx.proto\"@\n\nCapability\022\f\n\004name\030\001 \002(\t\022$\n\005value\030\002 \002(\0132\025.Mysqlx.Datatypes.Any\"I\n\fCapabilities\0223\n\fcapabilities\030\001 \003(\0132\035.Mysqlx.Connection.Capability:\004ê0\002\"\027\n\017CapabilitiesGet:\004ê0\001\"N\n\017CapabilitiesSet\0225\n\fcapabilities\030\001 \002(\0132\037.Mysqlx.Connection.Capabilities:\004ê0\002\"\r\n\005Close:\004ê0\003B\031\n\027com.mysql.cj.x.protobuf" };
    Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
          MysqlxConnection.descriptor = root;
          return null;
        }
      };
    Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[] { MysqlxDatatypes.getDescriptor(), 
          Mysqlx.getDescriptor() }, assigner);
  }
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Connection_Capability_descriptor = getDescriptor().getMessageTypes().get(0);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Connection_Capability_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Connection_Capability_descriptor, new String[] { "Name", "Value" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Connection_Capabilities_descriptor = getDescriptor().getMessageTypes().get(1);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Connection_Capabilities_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Connection_Capabilities_descriptor, new String[] { "Capabilities" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Connection_CapabilitiesGet_descriptor = getDescriptor().getMessageTypes().get(2);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Connection_CapabilitiesGet_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Connection_CapabilitiesGet_descriptor, new String[0]);
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Connection_CapabilitiesSet_descriptor = getDescriptor().getMessageTypes().get(3);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Connection_CapabilitiesSet_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Connection_CapabilitiesSet_descriptor, new String[] { "Capabilities" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Connection_Close_descriptor = getDescriptor().getMessageTypes().get(4);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Connection_Close_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Connection_Close_descriptor, new String[0]);
  
  private static Descriptors.FileDescriptor descriptor;
  
  static {
    ExtensionRegistry registry = ExtensionRegistry.newInstance();
    registry.add(Mysqlx.clientMessageId);
    registry.add(Mysqlx.serverMessageId);
    Descriptors.FileDescriptor.internalUpdateFileDescriptor(descriptor, registry);
    MysqlxDatatypes.getDescriptor();
    Mysqlx.getDescriptor();
  }
  
  public static interface CloseOrBuilder extends MessageOrBuilder {}
  
  public static interface CapabilitiesSetOrBuilder extends MessageOrBuilder {
    boolean hasCapabilities();
    
    MysqlxConnection.Capabilities getCapabilities();
    
    MysqlxConnection.CapabilitiesOrBuilder getCapabilitiesOrBuilder();
  }
  
  public static interface CapabilitiesGetOrBuilder extends MessageOrBuilder {}
  
  public static interface CapabilitiesOrBuilder extends MessageOrBuilder {
    List<MysqlxConnection.Capability> getCapabilitiesList();
    
    MysqlxConnection.Capability getCapabilities(int param1Int);
    
    int getCapabilitiesCount();
    
    List<? extends MysqlxConnection.CapabilityOrBuilder> getCapabilitiesOrBuilderList();
    
    MysqlxConnection.CapabilityOrBuilder getCapabilitiesOrBuilder(int param1Int);
  }
  
  public static interface CapabilityOrBuilder extends MessageOrBuilder {
    boolean hasName();
    
    String getName();
    
    ByteString getNameBytes();
    
    boolean hasValue();
    
    MysqlxDatatypes.Any getValue();
    
    MysqlxDatatypes.AnyOrBuilder getValueOrBuilder();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\x\protobuf\MysqlxConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */